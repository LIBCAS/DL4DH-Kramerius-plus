import { Collapse, IconButton, TableCell, TableRow } from '@mui/material'
import { FC, Fragment, useState } from 'react'
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'

export const CollapsibleTableRow: FC<{
	values: string[]
	children: JSX.Element
}> = ({ values, children }) => {
	const [open, setOpen] = useState(false)

	return (
		<Fragment>
			<TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
				<TableCell>
					<IconButton
						aria-label="expand row"
						size="small"
						onClick={() => setOpen(!open)}
					>
						{open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
					</IconButton>
				</TableCell>
				{values.map(value => (
					<TableCell key={value}>{value}</TableCell>
				))}
			</TableRow>
			<TableRow>
				<TableCell colSpan={6} style={{ paddingBottom: 0, paddingTop: 0 }}>
					<Collapse color="primary" in={open} timeout="auto" unmountOnExit>
						{children}
					</Collapse>
				</TableCell>
			</TableRow>
		</Fragment>
	)
}
