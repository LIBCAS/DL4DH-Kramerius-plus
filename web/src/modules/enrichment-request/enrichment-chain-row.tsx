import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
import { Collapse, IconButton, TableCell, TableRow } from '@mui/material'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { EnrichmentChain } from 'models/request/enrichment-chain'
import { FC, Fragment, useState } from 'react'
import { KrameriusJobTable } from './kramerius-job-table'

type Props = {
	chain: EnrichmentChain
	index: number
}

export const EnrichmentChainRow: FC<Props> = ({ chain, index }) => {
	const [open, setOpen] = useState<boolean>(false)

	return (
		<Fragment>
			<TableRow
				className="data-grid-row"
				hover
				sx={{ height: 20 }}
				onClick={() => setOpen(!open)}
			>
				<TableCell sx={{ padding: '0px 16px' }}>
					<IconButton
						aria-label="expand row"
						size="small"
						onClick={() => setOpen(!open)}
					>
						{open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
					</IconButton>
				</TableCell>
				<TableCell>{index + 1}</TableCell>
				<TableCell>{chain.publicationId}</TableCell>
				<TableCell>{chain.publicationTitle}</TableCell>
				<TableCell>{DigitalObjectModelMapping[chain.model]}</TableCell>
			</TableRow>
			<TableRow>
				<TableCell colSpan={6} sx={{ p: 0 }}>
					<Collapse in={open} timeout="auto" unmountOnExit>
						<KrameriusJobTable jobs={chain.jobs} />
					</Collapse>
				</TableCell>
			</TableRow>
		</Fragment>
	)
}
