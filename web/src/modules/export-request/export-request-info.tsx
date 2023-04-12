import { Button, Dialog, DialogContent, DialogTitle, Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { ExportRequest } from 'models/request/export-request'
import { FC, useState } from 'react'
import { Link } from 'react-router-dom'
import { formatDateTime } from 'utils/formatters'

export const ExportRequestInfo: FC<{ request: ExportRequest }> = ({
	request,
}) => {
	const [dialogContent, setDialogContent] = useState<string | null>(null)

	return (
		<PageBlock title="Základní informace">
			<Grid container spacing={0.5}>
				<KeyGridItem>Autor</KeyGridItem>
				<ValueGridItem>{request.owner.username}</ValueGridItem>
				<KeyGridItem>Dátum vytvoření</KeyGridItem>
				<ValueGridItem>{formatDateTime(request.created)}</ValueGridItem>
				<KeyGridItem>Název</KeyGridItem>
				<ValueGridItem>{request.name ?? '-'}</ValueGridItem>
				<KeyGridItem>Inicializační úloha</KeyGridItem>
				<ValueGridItem>
					<Link to={`/jobs/${request.createRequestJob.id}`}>
						{request.createRequestJob.executionStatus}
					</Link>
				</ValueGridItem>
				<KeyGridItem>Exportované publikace</KeyGridItem>
				<ValueGridItem>
					<Button
						size="small"
						sx={{ height: 20 }}
						variant="text"
						onClick={() =>
							setDialogContent(request.publicationIds.join(', \n'))
						}
					>
						Zobrazit
					</Button>
				</ValueGridItem>
			</Grid>
			<Dialog
				fullWidth
				open={!!dialogContent}
				onClose={() => setDialogContent(null)}
			>
				<DialogTitle>Publikace</DialogTitle>
				<DialogContent>
					<pre>{dialogContent}</pre>
				</DialogContent>
			</Dialog>
		</PageBlock>
	)
}
