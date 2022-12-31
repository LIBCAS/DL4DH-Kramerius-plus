import { List } from '@mui/material'
import { EnrichmentJobConfig } from '../../../models/job/config/enrichment-job-config'
import { ConfigListItem } from './config-list-item'

type Props = {
	configs: EnrichmentJobConfig[]
	onClick: (index: number) => void
	onRemove: (index: number) => void
}

export const ConfigList = ({ configs, onClick, onRemove }: Props) => {
	const handleOnClick = (index: number) => () => {
		onClick(index)
	}

	const handleOnRemove = (index: number) => () => {
		onRemove(index)
	}

	return (
		<List dense>
			{configs.map((config, i) => (
				<ConfigListItem
					key={i}
					config={config}
					index={i}
					onClick={handleOnClick(i)}
					onRemove={handleOnRemove(i)}
				/>
			))}
		</List>
	)
}
