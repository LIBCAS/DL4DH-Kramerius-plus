import { FC, ReactNode } from 'react'
import {
	Dialog,
	DialogTitle,
	DialogContent,
	DialogActions,
	Button,
} from '@mui/material'

interface Props {
	title: string
	children: ReactNode
	closeBtnLabel?: string
	minWidth?: number
	contentHeight?: number
	onSubmit: () => void
	open: boolean
	onClose: () => void
}

export const DefaultDialog: FC<Props> = ({
	title,
	children,
	closeBtnLabel = 'Zavřít',
	onSubmit,
	open,
	onClose,
}) => {
	return (
		<Dialog open={open} sx={{ minWidth: 400 }} onClose={onClose}>
			<DialogTitle>{title}</DialogTitle>
			<DialogContent>{children}</DialogContent>
			<DialogActions>
				<Button color="inherit" onClick={onClose}>
					{closeBtnLabel}
				</Button>
				<Button color="inherit" onClick={onSubmit}>
					Potvrdit
				</Button>
			</DialogActions>
		</Dialog>
	)
}
