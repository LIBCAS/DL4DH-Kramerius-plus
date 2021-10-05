import { useState, useMemo } from "react";
import Button from "@material-ui/core/Button";
import RadioGroup from "@material-ui/core/RadioGroup";
import Radio from "@material-ui/core/Radio";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Typography from "@material-ui/core/Typography";

import { DefaultDialog } from "../dialog/knav-dialog/knav-default-dialog";
import { DialogContentProps } from "../dialog/types";
import { Params, TeiParams } from "../../models";
import { JSONParams } from "./publication-export-json";
import { TEIParams } from "./publication-export-tei";

type ExportFormat = "json" | "tei";

const exportPublication = async (
  id: string,
  format: ExportFormat,
  params: Params | TeiParams
) => {
  await fetch(`/api/export/${id}/${format}`, {
    method: "POST",
    headers: new Headers({ "Content-Type": "application/json" }),
    body: JSON.stringify(params),
  });
};

const defaultJSONParams = {
  disablePagination: false,
  pageOffset: 0,
  pageSize: 20,
  filters: [],
  includeFields: [],
};

const defaultTeiParams = {
  disablePagination: false,
  pageOffset: 0,
  pageSize: 20,
  filters: [],
  includeFields: [],
  udPipeParams: [],
  nameTagParams: [],
  altoParams: [],
};

export const PublicationExportDialog = ({
  initialValues,
  onClose,
}: DialogContentProps<{
  id: string;
}>) => {
  const [format, setFormat] = useState<ExportFormat>("json");
  const [jsonParams, setJsonParams] = useState<Params>(defaultJSONParams);
  const [teiParams, setTeiParams] = useState<TeiParams>(defaultTeiParams);

  const [error, setError] = useState<string | undefined>();

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFormat((event.target as HTMLInputElement).value as ExportFormat);
  };

  const requestParams = useMemo(
    () => (format === "json" ? jsonParams : teiParams),
    [format, jsonParams, teiParams]
  );

  const handleSubmitExport = async () => {
    try {
      await exportPublication(initialValues!.id, format, requestParams);

      onClose();
    } catch (e) {
      setError("Při pokusu o export publikace došlo k chybě");
    }
  };

  return (
    <DefaultDialog
      title="Výběr formátu"
      actions={
        <Button color="primary" onClick={handleSubmitExport}>
          Potvrdit
        </Button>
      }
      minWidth={400}
    >
      <RadioGroup
        aria-label="export-format"
        name="format"
        value={format}
        onChange={handleChange}
      >
        <FormControlLabel
          value="json"
          control={<Radio color="primary" />}
          label="JSON"
        />
        <FormControlLabel
          value="tei"
          control={<Radio color="primary" />}
          label="TEI"
        />
      </RadioGroup>

      {format === "json" ? (
        <JSONParams params={jsonParams} setParams={setJsonParams} />
      ) : (
        <TEIParams params={teiParams} setParams={setTeiParams} />
      )}

      {error && (
        <Typography color="error" style={{ fontSize: 14, marginTop: 10 }}>
          {error}
        </Typography>
      )}
    </DefaultDialog>
  );
};
