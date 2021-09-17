import { useState } from "react";
import Paper from "@material-ui/core/Paper";
import AppBar from "@material-ui/core/AppBar";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Box from "@material-ui/core/Box";
import Grid from "@material-ui/core/Grid";
import { makeStyles } from "@material-ui/core/styles";

import { EventProps, EventDetail } from "../../components/event/event-detail";
import { useInterval } from "../../components/useInterval/use-interval";
import { getRunningTasks, getFinishedTasks } from "./enrichment-api";

interface TabPanelProps {
  children?: React.ReactNode;
  index: any;
  value: any;
}

const useStyles = makeStyles(() => ({
  paper: {
    padding: "10px 24px",
    minHeight: 140,
  },
  tab: {
    width: "50%",
    maxWidth: 500,
  },
}));

function a11yProps(index: any) {
  return {
    id: `simple-tab-${index}`,
    "aria-controls": `simple-tabpanel-${index}`,
  };
}

export const EnrichmentEvents = () => {
  const classes = useStyles();
  const [value, setValue] = useState(0);

  const handleChange = (event: React.ChangeEvent<{}>, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Paper className={classes.paper}>
      <AppBar position="static" component="div">
        <Tabs
          value={value}
          onChange={handleChange}
          aria-label="simple tabs example"
        >
          <Tab
            label="Aktuálně běžící procesy"
            {...a11yProps(0)}
            className={classes.tab}
          />
          <Tab
            label="Ukončené procesy"
            {...a11yProps(1)}
            className={classes.tab}
          />
        </Tabs>
      </AppBar>
      <TabPanel value={value} index={0}>
        <RunningEvents />
      </TabPanel>
      <TabPanel value={value} index={1}>
        <FinishedEvents />
      </TabPanel>
    </Paper>
  );
};

const TabPanel = (props: TabPanelProps) => {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && <Box paddingTop={2}>{children}</Box>}
    </div>
  );
};

const RunningEvents = () => {
  const [runningEvents, setRunningEvents] = useState<EventProps[]>([]);

  useInterval(async () => {
    const events = await getRunningTasks();

    setRunningEvents(events);
  }, 1000);

  if (runningEvents.length === 0) {
    return <p>Žádné běžící procesy.</p>;
  }

  return (
    <Grid container>
      {runningEvents.map((re, i) => (
        <EventDetail key={`${re.publication}-${i}`} {...re} />
      ))}
    </Grid>
  );
};

const FinishedEvents = () => {
  const [finishedEvents, setFinishedEvents] = useState<EventProps[]>([]);

  useInterval(async () => {
    const events = await getFinishedTasks();

    setFinishedEvents(events);
  }, 1000);

  if (finishedEvents.length === 0) {
    return <p>Žádné dokončené procesy.</p>;
  }

  return (
    <Grid container>
      {finishedEvents.map((re, i) => (
        <EventDetail key={`${re.publication}-${i}`} {...re} />
      ))}
    </Grid>
  );
};
