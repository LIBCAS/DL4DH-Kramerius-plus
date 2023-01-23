import { Button, Dialog, DialogContent, DialogTitle, Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { KrameriusJobMapping } from 'components/mappings/kramerius-job-mapping'
import { PageBlock } from 'components/page-block'
import { ValueGridItem } from 'components/value-grid-item'
import { ExportCsvJobConfig } from 'models/job/config/export-csv-job-config'
import { ExportJobConfig } from 'models/job/config/export-job-config'
import { ExportTeiJobConfig } from 'models/job/config/export-tei-job-config'
import { FC, Fragment, useState } from 'react'

export const ExportRequestConfig: FC<{ config: ExportJobConfig }> = ({
	config,
}) => {
	const [dialogContent, setDialogContent] = useState<string>()

	const onOpen = (content: string) => () => {
		setDialogContent(content)
	}

	const onClose = () => {
		setDialogContent(undefined)
	}

	const isExportCsv = (obj: ExportJobConfig): obj is ExportCsvJobConfig => {
		return obj.jobType === 'EXPORT_CSV'
	}

	const isExportTei = (obj: ExportJobConfig): obj is ExportTeiJobConfig => {
		return obj.jobType === 'EXPORT_TEI'
	}

	return (
		<PageBlock title="Konfigurace úlohy">
			<Grid container spacing={1}>
				<KeyGridItem>Typ úlohy</KeyGridItem>
				<ValueGridItem>{KrameriusJobMapping[config.jobType]}</ValueGridItem>
				<KeyGridItem>Parametre</KeyGridItem>
				<ValueGridItem>
					<Button
						disabled={!config.params}
						size="small"
						sx={{ height: 20 }}
						variant="text"
						onClick={onOpen(JSON.stringify(config.params, null, 2))}
					>
						Zobrazit
					</Button>
				</ValueGridItem>
				{isExportCsv(config) && (
					<Fragment>
						<KeyGridItem>Oddělovač</KeyGridItem>
						<ValueGridItem>
							{(config as ExportCsvJobConfig).delimiter}
						</ValueGridItem>
					</Fragment>
				)}
				{isExportTei(config) && (
					<Fragment>
						<KeyGridItem>TEI parametre</KeyGridItem>
						<ValueGridItem>
							<Button
								disabled={!config.params}
								size="small"
								sx={{ height: 20 }}
								variant="text"
								onClick={onOpen(JSON.stringify(config.teiParams, null, 2))}
							>
								Zobrazit
							</Button>
						</ValueGridItem>
					</Fragment>
				)}
			</Grid>
			<Dialog fullWidth open={!!dialogContent} onClose={onClose}>
				<DialogTitle>Parametre</DialogTitle>
				<DialogContent>
					<pre>{dialogContent}</pre>
				</DialogContent>
			</Dialog>
		</PageBlock>
	)
}
