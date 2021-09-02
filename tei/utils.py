from xml.dom import minidom
from xml.etree.ElementTree import tostring

calendar = {
    "leden": "01",
    "únor": "02",
    "březen": "03",
    "duben": "04",
    "květen": "05",
    "červen": "06",
    "červenec": "07",
    "srpen": "08",
    "září": "09",
    "říjen": "10",
    "listopad": "11",
    "prosinec": "12"
}


def month_to_number(month):
    if month.lower() in calendar:
        month = calendar[month.lower()]
    return month


def prettify(elem):
    rough_string = tostring(elem, 'utf-8')
    parsed = minidom.parseString(rough_string)
    return parsed.toprettyxml(indent="  ")
