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

def rows = table.select("tr");
rows.each {
  def country = it.select("td a").first()?.text()
  if (country) {
    println country
  }
}

assert table.size() == 1

