#!/usr/bin/env groovy
@Grapes(
  @Grab(group='org.jsoup', module='jsoup', version='1.8.3')
)

import groovy.util.XmlSlurper
import org.jsoup.Jsoup

// Fetch the table section @ https://en.wikipedia.org/wiki/ISO_3166-1

def raw = new URL("https://en.wikipedia.org/w/api.php?format=xml&action=parse&page=ISO_3166-1&prop=text&section=4").text
def api = new XmlSlurper().parseText(raw)
def doc = Jsoup.parse(api.parse.text.toString())
def table = doc.select("table");

assert table.size() == 1

def rows = table.select("tr");

new File("./src/main/resources/io.jvelo.turf",'countries.csv').withWriter('utf-8') { writer ->
  rows.each {
    def country = it.select("td a").first()?.text()
    if (country) {
      def alpha2 = it.select("td")[1].text()
      def alpha3 = it.select("td")[2].text()
      def numeric = it.select("td")[3].text()
      writer.writeLine([country, alpha2, alpha3, numeric].join(";"))
    }
  }
}


