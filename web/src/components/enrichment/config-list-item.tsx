import {
	ListItem,
	ListItemText,
	IconButton,
	ListItemAvatar,
	ListItemSecondaryAction,
} from '@mui/material'
import { EnrichmentJobConfig } from '../../models/job/config/enrichment-job-config'
import DeleteIcon from '@mui/icons-material/Delete'
import { Avatar } from '@material-ui/core'
import { ConfigPrimaryText } from './config-primary-text'
import { ConfigSecondaryText } from './config-secondary-text'

type Props = {
	config: EnrichmentJobConfig
	index: number
	onClick: () => void
	onRemove: () => void
}

export const ConfigListItem = ({ config, index, onClick, onRemove }: Props) => {
	return (
		<ListItem button onClick={onClick}>
			<ListItemAvatar>
				<Avatar>{index + 1}</Avatar>
			</ListItemAvatar>
			<ListItemText
				primary={<ConfigPrimaryText krameriusJob={config.jobType} />}
				secondary={<ConfigSecondaryText config={config} />}
			/>
			<ListItemSecondaryAction>
				<IconButton aria-label="delete" edge="end" onClick={onRemove}>
					<DeleteIcon />
				</IconButton>
			</ListItemSecondaryAction>
		</ListItem>
	)
}
