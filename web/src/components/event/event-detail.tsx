import Grid from '@material-ui/core/Grid'
import Paper from '@material-ui/core/Paper'
import Button from '@material-ui/core/Button'
import { cancelTask } from '../../modules/enrichment/enrichment-api'
import { EventDetailItem } from './event-detail-item'

export type EventProps = {
	publicationTitle?: string
	publicationId: string
	created: string
	started?: string
	processing?: string
	state?: string
	errorMessage?: string
	took?: string
	done?: string
}

export const EventDetail = ({
	publicationTitle,
	publicationId,
	processing,
	created,
	started,
	state,
	errorMessage,
	took,
	done,
}: EventProps) => {
	const disabledCancelButton = state !== 'ENRICHING'
	return (
		<Paper
			style={{
				width: '100%',
				padding: '10px 20px',
				marginBottom: 5,
			}}
		>
			<Grid container>
				<EventDetailItem title="Publikace" value={publicationTitle} />
				<EventDetailItem title="UUID" value={publicationId} />
				<EventDetailItem title="Status" value={state} />
				<EventDetailItem
					title="Vytvoření"
					value={new Date(created).toLocaleString()}
				/>
				<EventDetailItem
					title="Spuštění"
					value={started && new Date(started).toLocaleString()}
				/>
				<EventDetailItem title="Zpracovávání" value={processing} />
				<EventDetailItem title="Stav" value={done} />
				<EventDetailItem title="Chyba" value={errorMessage} />
				<EventDetailItem title="Trvanie" value={took} />
				<Grid>
					<Button
						color="primary"
						disabled={disabledCancelButton}
						type="submit"
						variant="contained"
						onClick={() => cancelTask(publicationId)}
					>
						Zrušit
					</Button>
				</Grid>
			</Grid>
		</Paper>
	)
}
