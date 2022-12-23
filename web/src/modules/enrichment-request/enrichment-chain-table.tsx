import {
	Box,
	Paper,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
	Typography,
} from '@mui/material'
import { EnrichmentChain } from 'models/request/enrichment-chain'
import { FC } from 'react'
import { EnrichmentChainRow } from './enrichment-chain-row'

type Props = {
	chains: EnrichmentChain[]
}

export const EnrichmentChainTable: FC<Props> = ({ chains }) => {
	return (
		<Box sx={{ margin: 1 }}>
			<Typography component="div" gutterBottom variant="h6">
				Sekvence úloh
			</Typography>
			<TableContainer component={Paper} variant="outlined">
				<Table size="small">
					<TableHead>
						<TableRow>
							<TableCell></TableCell>
							<TableCell>Pořadí</TableCell>
							<TableCell>UUID Publikace</TableCell>
							<TableCell>Název publikace</TableCell>
							<TableCell>Typ publikace</TableCell>
						</TableRow>
					</TableHead>
					<TableBody>
						{chains.map((chain, i) => (
							<EnrichmentChainRow key={chain.id} chain={chain} index={i} />
						))}
					</TableBody>
				</Table>
			</TableContainer>
		</Box>
	)
}
