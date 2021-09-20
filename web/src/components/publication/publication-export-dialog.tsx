import { useState } from "react";
import Button from "@material-ui/core/Button";
import RadioGroup from "@material-ui/core/RadioGroup";
import Radio from "@material-ui/core/Radio";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import Typography from "@material-ui/core/Typography";

import { DefaultDialog } from "../dialog/knav-dialog/knav-default-dialog";
import { DialogContentProps } from "../dialog/types";

type ExportFormat = "json" | "tie";

const exportPublication = async (id: string, format: ExportFormat) => {
  await fetch(`/api/export/${id}/${format}`, {
    method: "POST",
    headers: new Headers({ "Content-Type": "application/json" }),
  });
};

export const PublicationExportDialog = ({
  initialValues,
  onClose,
}: DialogContentProps<{
  id: string;
}>) => {
  const [format, setFormat] = useState<ExportFormat>("json");
  const [error, setError] = useState<string | undefined>();

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setFormat((event.target as HTMLInputElement).value as ExportFormat);
  };

  const handleSubmitExport = async () => {
    try {
      await exportPublication(initialValues!.id, format);

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
          value="tie"
          control={<Radio color="primary" />}
          label="TIE"
        />
      </RadioGroup>
      {error && (
        <Typography color="error" style={{ fontSize: 14, marginTop: 10 }}>
          {error}
        </Typography>
      )}
    </DefaultDialog>
  );
};
