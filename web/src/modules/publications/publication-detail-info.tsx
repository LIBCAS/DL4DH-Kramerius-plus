import { Button, Dialog, DialogContent, DialogTitle, Grid } from '@mui/material'
import { KeyGridItem } from 'components/key-grid-item'
import { useInfo } from 'components/navbar/info/info-context'
import { PublicationExportDialog } from 'components/publication/publication-export-dialog'
import { ReadOnlyField } from 'components/read-only-field/read-only-field'
import { ValueGridItem } from 'components/value-grid-item'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { Publication } from 'models'
import { FC, useState } from 'react'
import { formatDateTime } from 'utils/formatters'

type Props = {
	publication: Publication
	handlePublish: () => void
}

export const PublicationDetailInfo: FC<Props> = ({
	publication,
	handlePublish,
}) => {
	const [open, setOpen] = useState<boolean>(false)
	const [modsOpen, setModsOpen] = useState<boolean>(false)
	const [contextOpen, setContextOpen] = useState<boolean>(false)
	const [paradataOpen, setParadataOpen] = useState<boolean>(false)
	const { info } = useInfo()

	const onDialogOpen = () => {
		setOpen(true)
	}

	const onDialogClose = () => {
		setOpen(false)
	}

	const openInKramerius = () => {
		if (
			publication?.model === 'PERIODICAL' ||
			publication?.model === 'PERIODICAL_VOLUME'
		) {
			window.open(`${info?.kramerius.url}/periodical/${publication.id}`)
		} else {
			window.open(`${info?.kramerius.url}/view/${publication.id}`)
		}
	}

	return (
		<Grid container spacing={2} sx={{ p: 2 }}>
			<Grid container item justifyContent="space-between" spacing={4}>
				<Grid container item spacing={1} xl={4} xs={12}>
					<KeyGridItem>UUID</KeyGridItem>
					<ValueGridItem>{publication.id}</ValueGridItem>
					<KeyGridItem>Vytvořeno</KeyGridItem>
					<ValueGridItem>
						{publication.created &&
							formatDateTime(publication.created.toString())}
					</ValueGridItem>
					<KeyGridItem>Model</KeyGridItem>
					<ValueGridItem>
						{publication.model
							? DigitalObjectModelMapping[publication.model]
							: '?'}
					</ValueGridItem>
					<KeyGridItem>Název</KeyGridItem>
					<ValueGridItem>{publication.title}</ValueGridItem>
					<KeyGridItem>Počet stránek</KeyGridItem>
					<ValueGridItem>{publication.pageCount}</ValueGridItem>
					<KeyGridItem>Naposledy publikováno</KeyGridItem>
					<ValueGridItem>
						{publication.publishInfo.publishedLastModified
							? formatDateTime(
									publication.publishInfo.publishedLastModified.toString(),
							  )
							: '-'}
					</ValueGridItem>
				</Grid>
				<Grid container item spacing={1} xl={4} xs={12}>
					<KeyGridItem>Kontext</KeyGridItem>
					<ValueGridItem>
						<Button
							disabled={!publication.context}
							size="small"
							sx={{ height: 15 }}
							variant="text"
							onClick={() => setContextOpen(true)}
						>
							Zobrazit
						</Button>
					</ValueGridItem>
					<KeyGridItem>Kořenové UUID</KeyGridItem>
					<ValueGridItem>{publication.rootId ?? '-'}</ValueGridItem>
					<KeyGridItem>Kořenový název</KeyGridItem>
					<ValueGridItem>{publication.rootTitle ?? '-'}</ValueGridItem>
					<KeyGridItem>Kolekce</KeyGridItem>
					<ValueGridItem>
						{JSON.stringify(publication.collections, null, 2)}
					</ValueGridItem>
					<KeyGridItem>MODS metadáta</KeyGridItem>
					<ValueGridItem>
						<Button
							disabled={!publication.modsMetadata}
							size="small"
							sx={{ height: 15 }}
							variant="text"
							onClick={() => setModsOpen(true)}
						>
							Zobrazit
						</Button>
					</ValueGridItem>
					<KeyGridItem>Paradáta</KeyGridItem>
					<ValueGridItem>
						<Button
							disabled={!publication.paradata}
							size="small"
							sx={{ height: 15 }}
							variant="text"
							onClick={() => setParadataOpen(true)}
						>
							Zobrazit
						</Button>
					</ValueGridItem>
				</Grid>
				<Grid
					container
					item
					justifyContent="flex-end"
					spacing={1}
					xl={3}
					xs={12}
				>
					<Grid container direction="column" item spacing={1} xl={8}>
						<Grid item xs={1}>
							<Button
								color="primary"
								fullWidth
								variant="contained"
								onClick={openInKramerius}
							>
								Otevřít v Krameriovi
							</Button>
						</Grid>
						<Grid item xs={1}>
							<Button
								color="primary"
								fullWidth
								variant="contained"
								onClick={onDialogOpen}
							>
								Exportovat
							</Button>
						</Grid>
						<Grid item xs={1}>
							<Button
								color="primary"
								fullWidth
								variant="contained"
								onClick={handlePublish}
							>
								{publication?.publishInfo?.isPublished
									? 'Zrušit publikování'
									: 'Publikovat'}
							</Button>
						</Grid>
					</Grid>
				</Grid>
			</Grid>
			<PublicationExportDialog
				open={open}
				publicationIds={[publication.id]}
				onClose={onDialogClose}
			/>
			<Dialog fullWidth open={modsOpen} onClose={() => setModsOpen(false)}>
				<DialogTitle>MODS metadáta</DialogTitle>
				<DialogContent>
					<pre>{JSON.stringify(publication.modsMetadata, null, 2)}</pre>
				</DialogContent>
			</Dialog>
			<Dialog
				fullWidth
				open={contextOpen}
				onClose={() => setContextOpen(false)}
			>
				<DialogTitle>Kontext</DialogTitle>
				<DialogContent>
					<pre>{JSON.stringify(publication.context, null, 2)}</pre>
				</DialogContent>
			</Dialog>
			<Dialog
				fullWidth
				open={paradataOpen}
				onClose={() => setParadataOpen(false)}
			>
				<DialogTitle>Paradáta</DialogTitle>
				<DialogContent>
					<pre>{JSON.stringify(publication.paradata, null, 2)}</pre>
				</DialogContent>
			</Dialog>
		</Grid>
	)
}
