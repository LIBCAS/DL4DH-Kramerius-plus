import { ReactNode, useContext } from 'react'
import DialogTitle from '@material-ui/core/DialogTitle'
import DialogContent from '@material-ui/core/DialogContent'
import DialogActions from '@material-ui/core/DialogActions'
import Button from '@material-ui/core/Button'
import { makeStyles } from '@material-ui/core/styles'

import { DialogContext } from '../dialog-context'

interface Props {
	title: string
	children: ReactNode
	closeBtnLabel?: string
	minWidth?: number
	contentHeight?: number
	onSubmit: () => Promise<void>
}

type StyleProps = Pick<Props, 'minWidth' | 'contentHeight'>

const useStyles = makeStyles(() => ({
	root: {
		minWidth: (p: StyleProps) => p.minWidth,
	},
	title: {
		display: 'flex',
		alignItems: 'center',
		height: 30,
		backgroundColor: '#F0F3F4',
		'& > h2': {
			fontSize: 16,
		},
	},
	content: {
		height: p => p.contentHeight,
		'& *': {
			fontSize: 16,
		},
	},
	actions: {},
}))

export function DefaultDialog({
	title,
	children,
	closeBtnLabel = 'Zavřít',
	minWidth = 300,
	contentHeight = 200,
	onSubmit,
}: Props) {
	const { close } = useContext(DialogContext)
	const classes = useStyles({ minWidth, contentHeight })

	return (
		<div className={classes.root}>
			<DialogTitle className={classes.title}>{title}</DialogTitle>
			<DialogContent className={classes.content}>{children}</DialogContent>
			<DialogActions className={classes.actions}>
				<Button key="close" onClick={close}>
					{closeBtnLabel}
				</Button>
				<Button
					color="primary"
					onClick={() => {
						onSubmit()
						close()
					}}
				>
					Potvrdit
				</Button>
			</DialogActions>
		</div>
	)
}
