import { TextField } from '@mui/material'
import { GridCell, GridRow, GridRowProps } from '@mui/x-data-grid'
import { FC } from 'react'
import { Link } from 'react-router-dom'

export const LinkRow = (props: GridRowProps) => {
	console.log(props.row)
	return (
		<Link
			color="inherit"
			style={{ textDecoration: 'none', color: 'inherit' }}
			to="/"
		>
			<GridRow {...props} />
		</Link>
	)
}

export const LinkTextField = (props: any) => {
	return (
		<Link
			color="inherit"
			style={{ textDecoration: 'none', color: 'inherit' }}
			to="/"
		>
			<GridRow {...props} />
		</Link>
	)
}

export const LinkCell = (props: any) => {
	return (
		<Link
			color="inherit"
			style={{ textDecoration: 'none', color: 'inherit' }}
			to="/"
		>
			<GridCell {...props} />
		</Link>
	)
}
