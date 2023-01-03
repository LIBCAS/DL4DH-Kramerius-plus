import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
import {
	Button,
	Collapse,
	IconButton,
	Table,
	TableBody,
	TableCell,
	TableRow,
} from '@mui/material'
import { download } from 'api/file-ref-api'
import { DigitalObjectModelMapping } from 'enums/publication-model'
import { FC, Fragment, useState } from 'react'
import { ExportRowType } from './export-request-items'

export const ExportRequestItemRow: FC<{
	row: ExportRowType
	depth: number
}> = ({ row, depth }) => {
	const [open, setOpen] = useState<boolean>(false)

	const isDisabled = () => {
		return !row.children?.length
	}

	const onRowClick = () => {
		if (!isDisabled()) {
			setOpen(!open)
		}
	}

	const getColor = () => {
		switch (depth) {
			case 0:
				return 'white'
			case 1:
				return 'whiteSmoke'
			case 2:
				return 'lightGray'
			default:
				'black'
		}
	}

	return (
		<Fragment>
			<TableRow
				className="data-grid-row"
				hover
				sx={{ height: 20, backgroundColor: getColor() }}
				onClick={onRowClick}
			>
				<TableCell sx={{ padding: '0px 16px' }} width="50px">
					<IconButton
						aria-label="expand row"
						disabled={isDisabled()}
						size="small"
						onClick={onRowClick}
					>
						{open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
					</IconButton>
				</TableCell>
				<TableCell width="350px">{row.publicationId}</TableCell>
				<TableCell width="300px">{row.publicationTitle}</TableCell>
				<TableCell width="100px">
					{row.model ? DigitalObjectModelMapping[row.model] : ''}
				</TableCell>
				<TableCell width="90px">{row.state}</TableCell>
				<TableCell width="110px">
					{row.children ? row.children.length : 0}
				</TableCell>
				<TableCell width="130px">
					{row.exportJob ? row.exportJob.executionStatus : null}
				</TableCell>
				<TableCell width="100px">
					<Button
						disabled={!row.fileRef}
						size="small"
						sx={{ height: 20 }}
						variant="text"
						onClick={download(row.fileRef?.id ?? '')}
					>
						Stáhnout
					</Button>
				</TableCell>
			</TableRow>
			{row.children && (
				<TableRow>
					<TableCell colSpan={8} sx={{ p: 0 }}>
						<Collapse in={open} timeout="auto" unmountOnExit>
							<Table size="small">
								<TableBody>
									{row.children.map(child => (
										<ExportRequestItemRow
											key={child.id}
											depth={depth + 1}
											row={child}
										/>
									))}
								</TableBody>
							</Table>
						</Collapse>
					</TableCell>
				</TableRow>
			)}
		</Fragment>
	)
}
