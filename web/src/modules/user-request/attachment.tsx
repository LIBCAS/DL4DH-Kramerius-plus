import ClearIcon from '@mui/icons-material/Clear'
import { Box, Button, Stack, Typography } from '@mui/material'

export const Attachment = ({
	onRemove,
	file,
}: {
	onRemove: () => void
	file: File
}) => {
	return (
		<Box>
			<Stack alignItems="center" direction="row" gap={2}>
				<Typography variant="button">{file.name}</Typography>
				<Button color="error" variant="contained" onClick={onRemove}>
					<ClearIcon />
				</Button>
			</Stack>
		</Box>
	)
}
