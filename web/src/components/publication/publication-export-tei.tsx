import React from "react";
import Typography from "@material-ui/core/Typography";
import Grid from "@material-ui/core/Grid";
import { makeStyles } from "@material-ui/core/styles";

import { Sort, TeiParams } from "../../models";
import { CheckboxField } from "../checkbox-field/checkbox-field";
import { TextField } from "../text-field/text-field";
import { SelectField } from "../select-field/select-field";
import { Filter } from "../../models/filter";
import { AltoParam, PipeParam, TagParam } from "../../models/tei-params";

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
    includeFields = [],
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
          <SelectField<Filter>
            label="Filtre"
            name="filters"
            items={[]}
            value={filters}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                filters: v as Filter[],
              }))
            }
            optionMapper={(o) => `${o.field} - ${o.value}`}
          />
        </Grid>
        <Grid item xs={6}>
          <SelectField<Sort>
            label="Sort"
            name="sort"
            items={[]}
            value={sort}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                sort: v as Sort[],
              }))
            }
            optionMapper={(o) => `${o.field} - ${o.direction}`}
          />
        </Grid>
      </Grid>

      <Grid container spacing={2}>
        <Grid item xs={6}>
          <SelectField<string>
            label="Zahrnout pole"
            name="includeFields"
            items={[]}
            value={includeFields}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                includeFields: v as string[],
              }))
            }
            optionMapper={(o) => o}
          />
        </Grid>

        <Grid item xs={6}>
          <SelectField<PipeParam>
            label="udPipeParams"
            name="udPipeParams"
            items={[]}
            value={udPipeParams}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                udPipeParams: v as PipeParam[],
              }))
            }
            optionMapper={(o) => o}
          />
        </Grid>
      </Grid>

      <Grid container spacing={2}>
        <Grid item xs={6}>
          <SelectField<TagParam>
            label="nameTagParams"
            name="nameTagParams"
            items={[]}
            value={nameTagParams}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                nameTagParams: v as TagParam[],
              }))
            }
            optionMapper={(o) => o}
          />
        </Grid>
        <Grid item xs={6}>
          <SelectField<AltoParam>
            label="altoParams"
            name="altoParams"
            items={[]}
            value={altoParams}
            multiple
            onChange={(v) =>
              setParams((p) => ({
                ...p,
                altoParams: v as AltoParam[],
              }))
            }
            optionMapper={(o) => o}
          />
        </Grid>
      </Grid>
    </div>
  );
};
