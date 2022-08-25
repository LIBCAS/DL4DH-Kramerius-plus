import { AppBar, Toolbar, Container, Typography } from '@mui/material'
import { NavbarInfo } from './navbar-info'
import { ToolbarMenu } from './toolbar-menu'
import { FC } from 'react'
import { InstanceInfo } from 'models'

export type Page = {
	name: string
	label: string
	link: string
	onlyAuthenticated: boolean
}

const pages: Page[] = [
	{ name: 'Home', label: 'Domov', link: '/', onlyAuthenticated: false },
	{
		name: 'Enrichment',
		label: 'Obohacení',
		link: '/enrichment',
		onlyAuthenticated: true,
	},
	{
		name: 'Enrichment jobs',
		label: 'Úlohy obohacení',
		link: '/jobs/enriching',
		onlyAuthenticated: true,
	},
	{
		name: 'Publications',
		label: 'Publikace',
		link: '/publications',
		onlyAuthenticated: true,
	},
	{
		name: 'Export jobs',
		label: 'Úlohy exportování',
		link: '/jobs/exporting',
		onlyAuthenticated: true,
	},
	{
		name: 'Exports',
		label: 'Exporty',
		link: '/exports',
		onlyAuthenticated: true,
	},
]

export const Navbar: FC<{ info: InstanceInfo | null }> = ({ info }) => {
	return (
		<AppBar position="sticky">
			<Container maxWidth={false} sx={{ margin: '0' }}>
				<Toolbar
					disableGutters
					sx={{ display: 'flex', justifyContent: 'space-between' }}
				>
					<Typography
						component="a"
						href="/"
						noWrap
						sx={{
							mr: 1,
							display: 'flex',
							fontFamily: 'monospace',
							fontWeight: 500,
							letterSpacing: '.1rem',
							color: 'inherit',
							textDecoration: 'none',
						}}
						variant="h6"
					>
						Kramerius+ Client
					</Typography>
					<ToolbarMenu pages={pages} />
					<NavbarInfo
						instance={info?.kramerius.name}
						url={info?.kramerius.url}
						version={info?.krameriusPlus.version}
					/>
				</Toolbar>
			</Container>
		</AppBar>
	)
}
