import {
	GridCellParams,
	GridRenderCellParams,
	MuiEvent,
} from '@mui/x-data-grid'

export const onCellClick = (event: MuiEvent<React.MouseEvent<HTMLElement>>) => {
	const middleMouse = event.button === 1
	if (middleMouse) {
		event.stopPropagation()
	} else {
		event.preventDefault()
	}
}

export const renderGridCell = (value: string, path: string) => {
	return <a href={path} onClick={onCellClick}></a>
}
