import React from "react";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import { makeStyles } from "@material-ui/core/styles";

import { Sort, TeiParams } from "../../models";
import { CheckboxField } from "../checkbox-field/checkbox-field";
import { TextField } from "../text-field/text-field";
import { SelectField } from "../select-field/select-field";
import { AltoParam, PipeParam, TagParam } from "../../models/tei-params";
import {
  altoParamsOptions,
  nameTagParamsOptions,
  sortOptions,
  udPipeParamsOptions,
} from "./publication-items";
import { ExportFilters } from "./publication-export-filters";

type Props = {
  params: TeiParams;
  setParams: React.Dispatch<React.SetStateAction<TeiParams>>;
};

const useStyles = makeStyles(() => ({
  root: {
    marginTop: 20,
    marginBottom: 10,
    width: 400,
  },
}));

export const TEIParams = ({ setParams, params }: Props) => {
  const classes = useStyles();
  const {
    disablePagination = false,
    pageOffset = 0,
    pageSize = 20,
    filters = [],
    sort = [],
    udPipeParams = [],
    altoParams = [],
    nameTagParams = [],
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
        <Grid item xs={6}>
          <SelectField<AltoParam>
            label="altoParams"
            name="altoParams"
            items={altoParamsOptions}
            value={altoParams}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                altoParams: v as AltoParam[],
              }))
            }
            optionMapper={(o) => (o === "?" ? "jiný znak" : o)}
            labelMapper={(o) => (o === "?" ? "jiný znak" : o)}
          />
        </Grid>
      </Grid>

      <Grid container spacing={2}>
        <Grid item xs={6}>
          <SelectField<PipeParam>
            label="udPipeParams"
            name="udPipeParams"
            items={udPipeParamsOptions}
            value={udPipeParams}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                udPipeParams: v as PipeParam[],
              }))
            }
            optionMapper={(o) => (o === "?" ? "jiný znak" : o)}
            labelMapper={(o) => (o === "?" ? "jiný znak" : o)}
          />
        </Grid>

        <Grid item xs={6}>
          <SelectField<TagParam>
            label="nameTagParams"
            name="nameTagParams"
            items={nameTagParamsOptions}
            value={nameTagParams}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                nameTagParams: v as TagParam[],
              }))
            }
            optionMapper={(o) => (o === "?" ? "jiný znak" : o)}
            labelMapper={(o) => (o === "?" ? "jiný znak" : o)}
          />
        </Grid>
      </Grid>

      <ExportFilters filters={filters} setParams={setParams} />
    </div>
  );
};
