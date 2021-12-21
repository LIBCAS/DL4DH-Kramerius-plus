import { Grid, Typography } from '@material-ui/core'

type ItemProps = {
    title: string;
    value?: string;
}

export const EventDetailItem = ({
    title,
    value
}: ItemProps) => {
    if (value) {
        return (<>
            <Grid item xs={4}>
                <Typography>{title}:</Typography>
            </Grid>
            <Grid item xs={8}>
                <Typography color="primary">{value}</Typography>
            </Grid></>
        )
    } else {
        return <></>
    }
}