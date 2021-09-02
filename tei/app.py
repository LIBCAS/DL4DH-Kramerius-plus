from flask import Flask, request, jsonify
from converter import json_to_tei

app = Flask(__name__)


@app.route("/")
def root():
    return jsonify({
        "name": "TEI Converter",
        "version": "0.1"
    })


@app.route("/convert", methods=["POST"])
def convert():
    return json_to_tei(request.json)


if __name__ == '__main__':
    app.run()
