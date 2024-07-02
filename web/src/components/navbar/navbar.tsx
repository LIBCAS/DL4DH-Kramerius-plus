import {AppBar, Container, Toolbar} from '@mui/material'
import {FC} from 'react'
import {useInfo} from './info/info-context'
import {NavbarInfo} from './navbar-info'
import {NavbarUser} from './navbar-user'
import {ToolbarMenu} from './toolbar-menu'

export type NavbarInnerItem = {
	name: string
	label: string
	path: string
	onlyAuthenticated: boolean
}

export type NavbarItem = {
	name: string
	label: string
	path: string
	search?: string
	onlyAuthenticated: boolean
	children?: NavbarInnerItem[]
}

const pages: NavbarItem[] = [
	{
		name: 'New enrichment',
		label: 'Obohacení',
		path: '/enrichment/new',
		onlyAuthenticated: true,
	},
	{
		name: 'Enrichment requests',
		label: 'Žádosti obohacení',
		path: '/enrichment',
		onlyAuthenticated: true,
	},
	{
		name: 'Publications',
		label: 'Publikace',
		path: '/publications',
		onlyAuthenticated: true,
	},
	{
		name: 'Export requests',
		label: 'Žádosti exportu',
		path: '/exports',
		onlyAuthenticated: true,
	},
	{
		name: 'User requests',
		label: 'Užívatelské žádosti',
		path: '/requests',
		onlyAuthenticated: true,
	},
]

export const Navbar: FC = () => {
	const { info } = useInfo()
	return (
		<AppBar position="sticky">
			<Container maxWidth={false} sx={{ margin: '0' }}>
				<Toolbar
					disableGutters
					sx={{ display: 'flex', justifyContent: 'space-between' }}
				>
					<NavbarInfo
						lgWidth="20%"
						props={{
							instance: info?.kramerius.name as string,
							url: info?.kramerius.url as string,
							version: info?.krameriusPlus.version,
						}}
						xsWidth="60%"
					/>
					<ToolbarMenu lgWidth="70%" pages={pages} xsWidth="20%" />
					<NavbarUser width="20%" />
				</Toolbar>
			</Container>
		</AppBar>
	)
}
