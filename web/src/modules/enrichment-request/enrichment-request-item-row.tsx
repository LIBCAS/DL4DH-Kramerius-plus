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
import { EnrichmentRequestItem } from 'models/request/enrichment-request-item'
import { FC, Fragment, useState } from 'react'
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
import { EnrichmentChainTable } from './enrichment-chain-table'

type Props = {
	item: EnrichmentRequestItem
}

export const EnrichmentRequestItemRow: FC<Props> = ({ item }) => {
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
				<TableCell>{item.publicationId}</TableCell>
				<TableCell>{item.publicationTitle}</TableCell>
				<TableCell>{item.model}</TableCell>
				<TableCell>{item.enrichmentChains.length}</TableCell>
			</TableRow>
			<TableRow>
				<TableCell colSpan={6} style={{ paddingBottom: 0, paddingTop: 0 }}>
					<Collapse in={open} timeout="auto" unmountOnExit>
						<EnrichmentChainTable chains={item.enrichmentChains} />
					</Collapse>
				</TableCell>
			</TableRow>
		</Fragment>
	)
}
