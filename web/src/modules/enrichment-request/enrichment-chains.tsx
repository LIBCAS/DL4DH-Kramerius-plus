import {
	Box,
	Paper,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
} from '@mui/material'
import { PageBlock } from 'components/page-block'
import { EnrichmentChain } from 'models/request/enrichment-chain'
import { FC } from 'react'
import { EnrichmentChainRow } from './enrichment-chain-row'

export const EnrichmentChains: FC<{ chains: EnrichmentChain[] }> = ({
	chains,
}) => {
	return (
		<PageBlock title="Plány ve zvolené položke">
			{!!chains.length && (
				<Box display="flex" flexDirection="column" height={320}>
					<TableContainer component={Paper} elevation={0} variant="outlined">
						<Table size="small" stickyHeader>
							<TableHead>
								<TableRow>
									<TableCell width="5%"></TableCell>
									<TableCell>Pořadí</TableCell>
									<TableCell>UUID Publikace</TableCell>
									<TableCell>Název publikace</TableCell>
									<TableCell>Model</TableCell>
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
			)}
		</PageBlock>
	)
}
