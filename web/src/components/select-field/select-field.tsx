import React from "react";
import { makeStyles, Theme, createStyles } from "@material-ui/core/styles";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import InputLabel from "@material-ui/core/InputLabel";
import FormControl from "@material-ui/core/FormControl";
import { SelectClassKey } from "@material-ui/core/Select";
import { v4 } from "uuid";
import { get } from "lodash";

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      width: "100%",
      marginBottom: 8,
    },
    label: {
      padding: "0 5px",
      zIndex: 100,
      backgroundColor: "#fff",
      color: "black",
      fontSize: 16,
      fontWeight: "bold",
    },
  })
);

export interface Item {
  /**
   *
   */
  id?: string;

  /**
   *
   */
  label?: string;
}

export interface SelectFieldProps<TOption extends Item> {
  /**
   * Name of input.
   */
  name: string;

  /**
   * Label of input.
   */
  label?: string;

  /**
   * String or number value of input.
   */
  value: string | string[] | TOption | TOption[];

  /**
   * Change handler.
   */
  onChange: (value: string | string[] | TOption | TOption[]) => void;

  /**
   *
   */
  items: TOption[];

  /**
   *
   */
  disabled?: boolean;

  /**
   *
   */
  additionalClasses?: Partial<Record<SelectClassKey, string>>;

  /**
   * Multiple choices.
   */
  multiple?: boolean;

  /**
   * Returns whole object.
   *
   * Default is false.
   */
  valueIsObject?: boolean;

  optionMapper?: (o: TOption) => string;

  labelMapper?: (o: TOption) => string | undefined;
}

export function SelectField<TOption>({
  label,
  value,
  onChange,
  items = [],
  disabled,
  additionalClasses,
  multiple = false,
  optionMapper = (o) => get(o, "id", v4()),
  labelMapper = (o) => get(o, "label"),
}: SelectFieldProps<TOption>) {
  const classes = useStyles();

  /**
   * Change handler.
   */
  const handleChange = (
    event: React.ChangeEvent<{ name?: string | undefined; value: unknown }>
  ) => {
    const value = event.target.value as string;

    onChange(value);
  };

  return (
    <FormControl variant="outlined" className={classes.root}>
      <InputLabel className={classes.label} disabled={!!disabled} shrink>
        {label}
      </InputLabel>
      <Select
        value={value}
        onChange={handleChange}
        color="primary"
        error={false}
        spellCheck={true}
        lang="cs"
        disabled={!!disabled}
        classes={additionalClasses}
        multiple={multiple}
      >
        {items.map((item, index) => (
          <MenuItem key={optionMapper(item)} value={optionMapper(item)}>
            {labelMapper(item) ?? `[${index + 1}]`}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
}
