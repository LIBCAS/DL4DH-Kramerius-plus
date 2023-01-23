import { Button, Grid } from '@mui/material'
import { download } from 'api/file-ref-api'
import { KeyGridItem } from 'components/key-grid-item'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { BulkExport } from 'models/bulk-export'
import { FC } from 'react'
import { Link } from 'react-router-dom'

export const ExportRequestResult: FC<{ bulkExport?: BulkExport }> = ({
	bulkExport,
}) => {
	return (
		<PageBlock title="Výsledek hromadního exportu">
			<Grid container spacing={1}>
				<KeyGridItem>Stav</KeyGridItem>
				<ValueGridItem>{bulkExport?.state}</ValueGridItem>
				<KeyGridItem>Slučovací úloha</KeyGridItem>
				<ValueGridItem>
					{bulkExport?.mergeJob ? (
						<Link to={`/jobs/${bulkExport?.mergeJob.id}`}>
							{bulkExport?.mergeJob.executionStatus}
						</Link>
					) : (
						'-'
					)}
				</ValueGridItem>
				<KeyGridItem>Soubor</KeyGridItem>
				<ValueGridItem>
					<Button
						disabled={!bulkExport?.file}
						size="small"
						sx={{ height: 20 }}
						variant="text"
						onClick={download(bulkExport?.file?.id ?? '')}
					>
						Stáhnout
					</Button>
				</ValueGridItem>
			</Grid>
		</PageBlock>
	)
}
