import { List } from '@mui/material'
import { EnrichmentJobConfig } from 'models/job/config/enrichment-job-config'
import { ConfigListItem } from './config-list-item'

type Props = {
	configs?: EnrichmentJobConfig[]
	onClick: (id: string) => void
	onRemove: (id: string) => void
	disabled?: boolean
}

export const ConfigList = ({ configs, onClick, onRemove, disabled }: Props) => {
	const handleOnClick = (id: string) => () => {
		onClick(id)
	}

	const handleOnRemove = (id: string) => () => {
		onRemove(id)
	}

	return (
		<List dense>
			{configs &&
				configs.map((config, i) => (
					<ConfigListItem
						key={i}
						config={config}
						disabled={disabled}
						index={i}
						onClick={handleOnClick(config.id)}
						onRemove={handleOnRemove(config.id)}
					/>
				))}
		</List>
	)
}
