import {
	Box,
	Collapse,
	IconButton,
	Table,
	TableBody,
	TableCell,
	TableHead,
	TableRow,
	Typography,
} from '@mui/material'
import { EnrichmentChain } from 'models/request/enrichment-chain'

import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
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
			<TableRow>
				<TableCell>
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
				<TableCell align="right">{chain.model}</TableCell>
			</TableRow>
			<TableRow>
				<TableCell colSpan={6} style={{ paddingBottom: 0, paddingTop: 0 }}>
					<Collapse in={open} timeout="auto" unmountOnExit>
						<KrameriusJobTable jobs={chain.jobs} />
					</Collapse>
				</TableCell>
			</TableRow>
		</Fragment>
	)
}
