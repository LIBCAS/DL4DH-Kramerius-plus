from xml.etree.ElementTree import Element, SubElement
from utils import prettify, calendar, month_to_number


def generate_tei_header(kramerius_json):
    """
    Generate a TEI header element from Kramerius+ object

    :param kramerius_json: {'title': str, 'date': str}
    :returns: an XML element with tag `teiHeader`
    """
    tei_header = Element("teiHeader")

    file_desc = SubElement(tei_header, "fileDesc")
    title_stmt = SubElement(file_desc, "titleStmt")
    title = SubElement(title_stmt, "title")
    title.text = kramerius_json["title"]
    author = SubElement(title_stmt, "author")
    pers_name = SubElement(author, "orgName")  # TODO orgName vs persName
    pers_name.text = "Bratrstvo sv. Michala"  # TODO
    idno = SubElement(author, "idno", {"type": "mods:nameIdentifier"})
    idno.text = "kn20050602019"  # TODO

    extent = SubElement(file_desc, "extent")
    extent.text = "^^^sv. ; 23 cm"  # TODO

    publication_stmt = SubElement(file_desc, "publicationStmt")
    publisher = SubElement(publication_stmt, "publisher")
    publisher.text = "Bratrstvo sv. Michala"  # TODO
    pub_place = SubElement(publication_stmt, "pubPlace")
    pub_place.text = "V Praze"  # TODO
    if "date" in kramerius_json:
        date = SubElement(publication_stmt, "date")
        date.text = kramerius_json["date"]
    idno_data = {  # TODO
        "urnnbn": "urn:nbn:cz:mzk-006nu3",
        "barCode": "2610319748",
        "uuid": "3c4c3540-3130-11ea-b0e3-005056827e52",
        "ccnb": "cnb000905762",
        "oclc": "85385837"
    }
    for idno_type in idno_data:
        idno = SubElement(publication_stmt, "idno", {"type": "mods:" + idno_type})
        idno.text = idno_data[idno_type]
    availability = SubElement(publication_stmt, "availability")
    licence = SubElement(availability, "licence", {"resp": "#NameTag"})
    licence.text = "CC BY-NC-SA"
    availability = SubElement(publication_stmt, "availability")
    licence = SubElement(availability, "licence", {"resp": "#UDPipe"})
    licence.text = "CC BY-NC-SA"

    source_desc = SubElement(file_desc, "sourceDesc")
    SubElement(source_desc, "bibl")

    profile_desc = SubElement(tei_header, "profileDesc")
    text_class = SubElement(profile_desc, "textClass")
    class_code = SubElement(text_class, "classCode", {"scheme": "https://ufal.mff.cuni.cz/nametag/2/models"})
    interp_grp = SubElement(class_code, "interpGrp")
    interps = {
        "a": "ČÍSLA JAKO SOUČÁSTI ADRES",
        "ah": "číslo popisné",
        "at": "telefon, fax",
        "az": "PSČ",
        "g": "GEOGRAFICKÉ NÁZVY",
        "gc": "státní útvary",
        "gh": "vodní útvary",
        "gl": "přírodní oblasti / útvary",
        "gq": "části obcí, pomístní názvy",
        "gr": "menší územní jednotky",
        "gs": "ulice, náměstí",
        "gt": "kontinenty",
        "gu": "obce, hrady a zámky",
        "g_": "geografický název nespecifikovaného typu / nezařaditelný do ostatních typů",
        "i": "NÁZVY INSTITUCÍ",
        "ia": "přednášky, konference, soutěže,...",
        "ic": "kulturní, vzdělávací a vědecké instituce, sportovní kluby,...",
        "if": "firmy, koncerny, hotely,...",
        "io": "státní a mezinárodní instituce, politické strany a hnutí, náboženské skupiny",
        "i_": "instituce nespecifikovaného typu / nezařaditelné do ostatních typů",
        "m": "NÁZVY MÉDIÍ",
        "me": "e-mailové adresy",
        "mi": "internetové odkazy",
        "mn": "periodika, redakce, tiskové agentury",
        "ms": "rozhlasové a televizní stanice",
        "n": "ČÍSLA SE SPECIFICKÝM VÝZNAMEM",
        "na": "věk",
        "nc": "číslo s významem počtu",
        "nb": "číslo strany, kapitoly, oddílu, obrázku",
        "no": "číslo s významem pořadí",
        "ns": "sportovní skóre",
        "ni": "itemizátor",
        "n_": "číslo se specifickým významem, jehož typ nebyl vyčleněn jako samostatný / nelze identifikovat",
        "o": "NÁZVY VĚCÍ",
        "oa": "kulturní artefakty (knihy, filmy stavby,...)",
        "oe": "měrné jednotky (zapsané zkratkou)",
        "om": "měny (zapsané zkratkou, symbolem)",
        "op": "výrobky",
        "or": "předpisy, normy,..., jejich sbírky",
        "o_": "názvy nespecifikovaného typu / nezařaditelné do ostatních typů",
        "p": "JMÉNA OSOB",
        "pc": "obyvatelská jména",
        "pd": "titul (pouze zkratkou)",
        "pf": "křestní jméno",
        "pm": "druhé křestní jméno",
        "pp": "náboženské postavy, pohádkové a mytické postavy, personifikované vlastnosti",
        "ps": "příjmení",
        "p_": "jméno osoby nespecifikovaného typu / nezařaditelné do ostatních typů",
        "t": "ČASOVÉ ÚDAJE",
        "td": "den",
        "tf": "svátky a významné dny",
        "th": "hodina",
        "tm": "měsíc",
        "ty": "rok"
    }
    for interp in interps:
        interp_element = SubElement(interp_grp, "interp", {"xml:id": "nametag-" + interp})
        interp_element.text = interps[interp]

    lang_usage = SubElement(profile_desc, "langUsage")
    language = SubElement(lang_usage, "language", {"ident": "cze"})
    language.text = "cze"
    return tei_header


def json_to_tei(kramerius):
    # Create top XML document
    tei = Element("TEI", {"xmlns": "https://www.tei-c.org/ns/1.0"})

    # Create teiHeader
    tei_header = generate_tei_header(kramerius)
    tei.append(tei_header)

    # Create facsimile
    # facsimile = SubElement(tei, "facsimile")
    # SubElement(facsimile, "graphic", {"url": ""})

    # Create text
    text = SubElement(tei, "text")
    body = SubElement(text, "body")

    # Create pages
    for page in kramerius["pages"]:
        # Create page division with page break
        div = SubElement(body, "div")
        SubElement(div, "pb", {"n": page["title"]})
        p = SubElement(div, "p")

        # Stack
        stack = []

        # Copy tokens
        token_position_in_sentence = None
        for token in page["tokens"]:
            # Create paragraph for every sentence
            linguistic_metadata = token["linguisticMetadata"]
            if token_position_in_sentence is None or token_position_in_sentence > linguistic_metadata["position"]:
                s = SubElement(p, "s")
                stack = [s]  # Clear the stack
            token_position_in_sentence = linguistic_metadata["position"]

            # Check nameTag info
            name_tags = []
            if "nameTagMetadata" in token:
                name_tags = token["nameTagMetadata"].split("|")

            if len(name_tags) > 0 or len(stack) > 1:
                # Count continued tags
                continued = 1
                for nameTag in name_tags:
                    if "I-" in nameTag:
                        continued += 1
                # Removed not continuing tags
                stack = stack[0:continued]
                # For each new group, add new tag to stack
                for nameTag in name_tags:
                    if "B-" in nameTag:
                        grp_name = nameTag[2:]
                        if grp_name == "ah" or \
                                grp_name == "na" or \
                                grp_name == "nc" or \
                                grp_name == "nb" or \
                                grp_name == "ns" or \
                                grp_name == "ni" or \
                                grp_name == "n_":
                            grp = SubElement(stack[-1], "num")
                        elif grp_name == "at":
                            grp = SubElement(stack[-1], "num", {"type": "phone"})
                        elif grp_name == "az":
                            grp = SubElement(stack[-1], "num", {"type": "zip"})
                        elif grp_name == "c" or \
                                grp_name == "C":
                            grp = SubElement(stack[-1], "objectName", {"type": "bibliography"})
                        elif grp_name == "gc":
                            grp = SubElement(stack[-1], "placeName")
                            grp = SubElement(grp, "country")
                        elif grp_name == "gh":
                            grp = SubElement(stack[-1], "geogName", {"type": "water"})
                        elif grp_name == "gl":
                            grp = SubElement(stack[-1], "geogName", {"type": "area"})
                        elif grp_name == "gq" or grp_name == "gu":
                            grp = SubElement(stack[-1], "placeName")
                            grp = SubElement(grp, "settlement")
                        elif grp_name == "gr":
                            grp = SubElement(stack[-1], "placeName")
                            grp = SubElement(grp, "region")
                        elif grp_name == "gs":
                            grp = SubElement(stack[-1], "address")
                            grp = SubElement(grp, "street")
                        elif grp_name == "gt":
                            grp = SubElement(stack[-1], "geogName", {"type": "continent"})
                        elif grp_name == "g_":
                            grp = SubElement(stack[-1], "placeName")
                        elif grp_name == "ia" or \
                                grp_name == "o_" or \
                                grp_name == "p_":
                            grp = SubElement(stack[-1], "objectName")
                        elif grp_name == "ic" or \
                                grp_name == "if" or \
                                grp_name == "io" or \
                                grp_name == "i_" or \
                                grp_name == "mn" or \
                                grp_name == "ms":
                            grp = SubElement(stack[-1], "orgName")
                        elif grp_name == "me":
                            grp = SubElement(stack[-1], "email")
                        elif grp_name == "mi":
                            grp = SubElement(stack[-1], "ref", {"target": token["content"]})
                        elif grp_name == "no":
                            grp = SubElement(stack[-1], "num", {"type": "ordinal"})
                        elif grp_name == "oa":
                            grp = SubElement(stack[-1], "objectName", {"type": "artefact"})
                        elif grp_name == "oe" or \
                                grp_name == "om":
                            grp = SubElement(stack[-1], "unit")
                        elif grp_name == "op":
                            grp = SubElement(stack[-1], "objectName", {"type": "product"})
                        elif grp_name == "or":
                            grp = SubElement(stack[-1], "objectName", {"type": "rule"})
                        elif grp_name == "pc":
                            grp = SubElement(stack[-1], "objectName", {"type": "population"})
                        elif grp_name == "pd":
                            grp = SubElement(stack[-1], "abbr")
                        elif grp_name == "pf":
                            grp = SubElement(stack[-1], "forename")
                        elif grp_name == "pm":
                            grp = SubElement(stack[-1], "forename", {"type": "middle"})
                        elif grp_name == "pp":
                            grp = SubElement(stack[-1], "persName")
                        elif grp_name == "ps":
                            grp = SubElement(stack[-1], "surname")
                        elif grp_name == "t" or \
                                grp_name == "T":
                            grp = SubElement(stack[-1], "date")
                        elif grp_name == "td":
                            try:
                                grp = SubElement(stack[-1], "date", {"when": "---%02d" % int(token["content"])})
                            except ValueError:
                                grp = SubElement(stack[-1], "date")
                        elif grp_name == "tm":
                            month = month_to_number(linguistic_metadata["lemma"])
                            try:
                                grp = SubElement(stack[-1], "date", {"when": "--%02d" % int(month)})
                            except ValueError:
                                grp = SubElement(stack[-1], "date")
                        elif grp_name == "ty":
                            grp = SubElement(stack[-1], "date", {"when": token["content"]})
                        elif grp_name == "tf":
                            grp = SubElement(stack[-1], "date", {"type": "holiday"})
                        elif grp_name == "th":
                            grp = SubElement(stack[-1], "time")
                        else:
                            grp = SubElement(stack[-1], "group", {"type": grp_name})
                        grp.set("ana", "#nametag-" + grp_name)
                        stack.append(grp)

            # Add a token
            # TODO REFACTOR
            if linguistic_metadata["uPosTag"] == "PUNCT":
                not_space_after = "SpaceAfter=No" in linguistic_metadata["misc"]
                attrs = {
                    "n": str(linguistic_metadata["position"]),
                    "pos": linguistic_metadata["uPosTag"],
                    "join": "both" if not_space_after else "left"
                }
                if "feats" in linguistic_metadata:
                    attrs["msd"] = linguistic_metadata["feats"]
                if "lemma" in linguistic_metadata:
                    attrs["lemma"] = linguistic_metadata["lemma"]
                pc = SubElement(stack[-1], "pc", attrs)
                pc.text = token["content"]
            else:
                attrs = {
                    "n": str(linguistic_metadata["position"]),
                    "lemma": linguistic_metadata["lemma"],
                    "pos": linguistic_metadata["uPosTag"]
                }
                if "feats" in linguistic_metadata:
                    attrs["msd"] = linguistic_metadata["feats"]
                w = SubElement(stack[-1], "w", attrs)
                w.text = token["content"]

    # postprocessing
    for grp in tei.findall(".//p"):
        children = list(grp)
        if len(children) == 1 and children[0].tag == "s":
            children_of_s = list(children[0])
            if len(children_of_s) == 1 and \
                    children_of_s[0].tag == "objectName" and \
                    children_of_s[0].get("type", "") == "bibliography":
                children[0].remove(children_of_s[0])
                children_of_s[0].tag = "bibl"
                children_of_s[0].attrib.pop("type")
                grp.append(children_of_s[0])
        # TODO Check if parent is S and its parent is P and S contains only this element
        # then remove S and move this element to P
    for grp in tei.findall(".//group[@ana='#nametag-P']"):
        children = set(map(lambda e: e.tag, list(grp)))
        children.discard("forename")
        children.discard("placename")
        children.discard("abbr")
        children.discard("w")
        children.discard("pc")
        if len(children) == 0:
            grp.tag = "persName"
            grp.attrib.pop("type")
            grp.attrib.pop("ana")
    for grp in tei.findall(".//date[@ana='#nametag-T']"):
        td = grp.find("date[@ana='#nametag-td']")
        tm = grp.find("date[@ana='#nametag-tm']")
        ty = grp.find("date[@ana='#nametag-ty']")
        if td is not None and tm is not None and ty is not None:
            grp.tag = "date"
            # grp.attrib.pop("type")
            date_elements = ["", "", ""]
            for sub in td.iter():
                if sub is not td:
                    sub.set("ana", "#nametag-td")
                    grp.append(sub)
                    if sub.tag == "w":
                        date_elements[2] = sub.attrib["lemma"]
            for sub in tm.iter():
                if sub is not tm:
                    sub.set("ana", "#nametag-tm")
                    grp.append(sub)
                    if sub.tag == "w" and sub.attrib["lemma"].lower() in calendar:
                        date_elements[1] = calendar[sub.attrib["lemma"]]
            for sub in ty.iter():
                if sub is not ty:
                    sub.set("ana", "#nametag-ty")
                    grp.append(sub)
                    if sub.tag == "w":
                        date_elements[0] = sub.attrib["lemma"]
            grp.remove(td)
            grp.remove(tm)
            grp.remove(ty)
            if len(date_elements[1]) == 1:
                date_elements[1] = "0" + date_elements[1]
            if len(date_elements[2]) == 1:
                date_elements[2] = "0" + date_elements[2]
            if len(date_elements[0]) == 4 and len(date_elements[1]) == 2 and len(date_elements[2]) == 2:
                grp.set("when", "-".join(date_elements))
            if len(date_elements[0]) == 0 and len(date_elements[1]) == 2 and len(date_elements[2]) == 2:
                grp.set("when", "-%s-%s" % (date_elements[1], date_elements[2]))
            if len(date_elements[0]) == 0 and len(date_elements[1]) == 0 and len(date_elements[2]) == 2:
                grp.set("when", "---%s" % (date_elements[0]))
            if len(date_elements[0]) == 0 and len(date_elements[1]) == 2 and len(date_elements[2]) == 0:
                grp.set("when", "--%s" % (date_elements[1]))
            if len(date_elements[0]) == 4 and len(date_elements[1]) == 2 and len(date_elements[2]) == 0:
                grp.set("when", "%s-%s" % (date_elements[0], date_elements[1]))
            if len(date_elements[0]) == 4 and len(date_elements[1]) == 0 and len(date_elements[2]) == 0:
                grp.set("when", date_elements[0])
    return prettify(tei)
