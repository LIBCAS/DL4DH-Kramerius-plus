import React from "react";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import { makeStyles } from "@material-ui/core/styles";

import { Params, Sort } from "../../models";
import { CheckboxField } from "../checkbox-field/checkbox-field";
import { TextField } from "../text-field/text-field";
import { SelectField } from "../select-field/select-field";
import { fieldOptions, sortOptions } from "./publication-items";
import { ExportFilters } from "./publication-export-filters";

type Props = {
  params: Params;
  setParams: React.Dispatch<React.SetStateAction<Params>>;
};

const useStyles = makeStyles(() => ({
  root: {
    marginTop: 20,
    marginBottom: 10,
    width: 400,
  },
}));

export const JSONParams = ({ setParams, params }: Props) => {
  const classes = useStyles();
  const {
    disablePagination = false,
    pageOffset = 0,
    pageSize = 20,
    filters = [],
    sort = [],
    includeFields = [],
  } = params;

  return (
    <div className={classes.root}>
      <Typography>Parametry</Typography>

      <Grid container>
        <CheckboxField
          label="Zakázat stránkování"
          name="disablePagination"
          value={disablePagination}
          onChange={(v) => setParams((p) => ({ ...p, disablePagination: v }))}
        />
      </Grid>

      <Grid container spacing={2}>
        <Grid item xs={6}>
          <TextField
            label="Stránka"
            name="pageOffset"
            value={pageOffset}
            onChange={(v) =>
              setParams((p) => ({ ...p, pageOffset: Number(v) }))
            }
            type="number"
            disabled={disablePagination}
          />
        </Grid>

        <Grid item xs={6}>
          <TextField
            label="Záznamy na stránku"
            name="pageSize"
            value={pageSize}
            onChange={(v) => setParams((p) => ({ ...p, pageSize: Number(v) }))}
            type="number"
            disabled={disablePagination}
          />
        </Grid>
      </Grid>

      <Grid container spacing={2}>
        <Grid item xs={6}>
          <SelectField<{ id: string; label: string }>
            label="Zahrnout pole"
            name="includeFields"
            items={fieldOptions}
            value={includeFields}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                includeFields: v as string[],
              }))
            }
            optionMapper={(o) => o.id}
          />
        </Grid>
        <Grid item xs={6}>
          <SelectField<Sort>
            label="Sort"
            name="sort"
            items={sortOptions}
            value={sort}
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                sort: v as Sort,
              }))
            }
            optionMapper={(o) => o.direction}
            labelMapper={(o) => o.direction}
          />
        </Grid>
      </Grid>
      <ExportFilters filters={filters} setParams={setParams} />
    </div>
  );
};
