import DownloadIcon from '@mui/icons-material/Download'
import PublishIcon from '@mui/icons-material/Publish'
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline'
import UnpublishedIcon from '@mui/icons-material/Unpublished'
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
		<Button endIcon={<PublishIcon />} variant="text" onClick={onClick}>
			Publikovat
		</Button>
	)
}

const GridToolbarUnpublishButton: FC<{ onClick: () => void }> = ({
	onClick,
}) => {
	return (
		<Button endIcon={<UnpublishedIcon />} variant="text" onClick={onClick}>
			Zrušit publikování
		</Button>
	)
}

const GridToolbarExportButton: FC<{ onClick: () => void }> = ({ onClick }) => {
	return (
		<Button endIcon={<DownloadIcon />} variant="text" onClick={onClick}>
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
