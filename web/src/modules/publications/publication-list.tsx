import { Grid } from '@mui/material'
import { GridSelectionModel } from '@mui/x-data-grid'
import { PublicationExportDialog } from 'components/publication/publication-export-dialog'
import { Publication } from 'models'
import { ChangeEvent, useEffect, useState } from 'react'
import { listPublications } from '../../api/publication-api'
import { PublicationListFilter } from './publication-list-filter'
import { PublicationListTable } from './publication-list-table'

type Props = {
	onRowClick: (publicationId: string) => void
}

type PublicationFilter = {
	title?: string
	parentId?: string
	createdBefore?: Date
	createdAfter?: Date
	isPublished?: boolean
	publishedBefore?: Date
	publishedAfter?: Date
}

export const PublicationList = ({ onRowClick }: Props) => {
	const [rowCount, setRowCount] = useState<number>()
	const [publications, setPublications] = useState<Publication[]>([])
	const [selectedPublications, setSelectedPublications] = useState<string[]>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)
	const [filter, setFilter] = useState<PublicationFilter>({})
	const [open, setOpen] = useState<boolean>(false)

	const onDialogOpen = () => {
		setOpen(true)
	}

	const onDialogClose = () => {
		setOpen(false)
	}

	useEffect(() => {
		const fetchPublications = async () => {
			const response = await listPublications(page, 10, filter)

			if (response) {
				setPublications(response.results)
				setRowCount(response.total)
			}
		}
		fetchPublications()
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [page])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	const onFilterClick = () => {
		const fetchPublications = async () => {
			const response = await listPublications(page, 10, filter)

			if (response) {
				setPublications(response.results)
				setRowCount(response.total)
			}
		}
		fetchPublications()
	}

	const onTextChange =
		(key: keyof PublicationFilter) =>
		(event: ChangeEvent<HTMLInputElement>) => {
			setFilter({ ...filter, [key]: event.target.value })
		}

	const onDateChange =
		(key: keyof PublicationFilter) => (value: Date | null) => {
			setFilter({ ...filter, [key]: value })
		}

	const onPublishedChange = (
		event: React.MouseEvent<HTMLElement>,
		isPublished: boolean,
	) => {
		setFilter({ ...filter, isPublished: isPublished })
	}

	const onSelectionChange = (selectionModel: GridSelectionModel) => {
		setSelectedPublications(selectionModel.map(model => model.toString()))
	}

	return (
		<Grid container spacing={2}>
			<Grid item xs={12}>
				<PublicationListFilter
					filter={filter}
					onDateChange={onDateChange}
					onExportClick={onDialogOpen}
					onFilterClick={onFilterClick}
					onPublishedChange={onPublishedChange}
					onTextChange={onTextChange}
				/>
			</Grid>
			<Grid item xs={12}>
				<PublicationListTable
					publications={publications}
					rowCount={rowCountState}
					onPageChange={onPageChange}
					onRowClick={onRowClick}
					onSelectionChange={onSelectionChange}
				/>
			</Grid>
			<PublicationExportDialog
				open={open}
				publicationIds={selectedPublications}
				onClose={onDialogClose}
			/>
		</Grid>
	)
}
