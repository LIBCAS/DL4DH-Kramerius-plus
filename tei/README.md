# Requirements

- python 3 (tested with 3.8)
- python venv (`apt install python3.8-venv`)

# Development

- create a virtual environment (venv): `python3 -m venv venv` (or `py -3 -m venv venv` on Windows)
- activate the venv: `. ./venv/bin/activate` (or `venv\Scripts\activate` on Windows)
- install requirements: `pip install -r requirements.txt`
- run the server: `export FLASK_APP=app && flask run`
- if you want to exit the venv: `deactivate`


# Test the endpoints

### Get version of the TEI Converter

`curl http://127.0.0.1:5000/`

### Convert Kramerius+ JSON to TEI

`curl -X POST -H "Content-Type: application/json" -d @input_file.json http://127.0.0.1:5000/convert`
