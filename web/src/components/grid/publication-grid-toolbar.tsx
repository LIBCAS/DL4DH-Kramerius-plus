import ExitToAppIcon from '@mui/icons-material/ExitToApp'
import PublicIcon from '@mui/icons-material/Public'
import PublicOffIcon from '@mui/icons-material/PublicOff'
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline'
import { Button } from '@mui/material'
import { GridToolbarContainer } from '@mui/x-data-grid'
import { FC } from 'react'

const GridToolbarClearSelectionButton: FC<{ onClick: () => void }> = ({
	onClick,
}) => {
	return (
		<Button
			endIcon={<RemoveCircleOutlineIcon />}
			variant="text"
			onClick={onClick}
		>
			Zrušit výběr
		</Button>
	)
}

const GridToolbarPublishButton: FC<{ onClick: () => void }> = ({ onClick }) => {
	return (
		<Button endIcon={<PublicIcon />} variant="text" onClick={onClick}>
			Publikovat
		</Button>
	)
}

const GridToolbarUnpublishButton: FC<{ onClick: () => void }> = ({
	onClick,
}) => {
	return (
		<Button endIcon={<PublicOffIcon />} variant="text" onClick={onClick}>
			Zrušit publikování
		</Button>
	)
}

const GridToolbarExportButton: FC<{ onClick: () => void }> = ({ onClick }) => {
	return (
		<Button endIcon={<ExitToAppIcon />} variant="text" onClick={onClick}>
			Exportovat
		</Button>
	)
}

export const PublicationGridToolbar: FC<{
	onClearSelection: () => void
	onExportClick: () => void
	onPublishClick: () => void
	onUnpublishClick: () => void
}> = ({
	onExportClick,
	onPublishClick,
	onUnpublishClick,
	onClearSelection,
}) => {
	return (
		<GridToolbarContainer>
			<GridToolbarClearSelectionButton onClick={onClearSelection} />
			<GridToolbarPublishButton onClick={onPublishClick} />
			<GridToolbarUnpublishButton onClick={onUnpublishClick} />
			<GridToolbarExportButton onClick={onExportClick} />
		</GridToolbarContainer>
	)
}
