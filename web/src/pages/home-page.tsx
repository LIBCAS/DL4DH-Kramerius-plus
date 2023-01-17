import { Box, Typography, Grid, Button } from '@mui/material'
import { useKeycloak } from '@react-keycloak/web'
import { FC, useCallback } from 'react'
import { PageWrapper } from './page-wrapper'

export const HomePage: FC = () => {
	const { keycloak } = useKeycloak()

	const login = useCallback(() => {
		keycloak?.login()
	}, [keycloak])

	return (
		<PageWrapper>
			<Box
				sx={{
					p: 10,
				}}
			>
				<Grid alignItems="center" container direction="column" spacing={5}>
					<Grid item>
						<Typography color="primary" component="div" variant="h5">
							Vítejte v klientu Kramerius+!
						</Typography>
					</Grid>
					{!keycloak.authenticated && (
						<>
							<Grid item>
								<Typography component="span" variant="body1">
									Pro pokračování se nejdříve musíte přihlásit.
								</Typography>
							</Grid>
							<Grid item>
								<Button variant="contained" onClick={login}>
									Přihlásit
								</Button>
							</Grid>
						</>
					)}
				</Grid>
			</Box>
		</PageWrapper>
	)
}
