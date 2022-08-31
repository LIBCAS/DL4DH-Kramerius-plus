import { AppBar, Toolbar, Container } from '@mui/material'
import { NavbarUser } from './navbar-user'
import { NavbarInfo } from './navbar-info'
import { ToolbarMenu } from './toolbar-menu'
import { FC } from 'react'
import { InstanceInfo } from 'models'

export type NavbarInnerItem = {
	name: string
	label: string
	link: string
	onlyAuthenticated: boolean
}

export type NavbarItem = {
	name: string
	label: string
	link?: string
	onlyAuthenticated: boolean
	children?: NavbarInnerItem[]
}

const pages: NavbarItem[] = [
	{ name: 'Home', label: 'Domov', link: '/', onlyAuthenticated: false },
	{
		name: 'Enrichment',
		label: 'Obohacení',
		link: '/enrichment',
		onlyAuthenticated: true,
		children: [
			{
				name: 'New enrichment',
				label: 'Nové obohacení',
				link: '/enrichment',
				onlyAuthenticated: true,
			},
			{
				name: 'Enrichment plans',
				label: 'Plány úloh obohacení',
				link: '/job-plans/enriching',
				onlyAuthenticated: true,
			},
			{
				name: 'Enrichment jobs',
				label: 'Úlohy obohacení',
				link: '/jobs/enriching',
				onlyAuthenticated: true,
			},
		],
	},
	{
		name: 'Publications',
		label: 'Publikace',
		link: '/publications',
		onlyAuthenticated: true,
	},
	{
		name: 'Export',
		label: 'Export',
		onlyAuthenticated: true,
		children: [
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
		],
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
					<NavbarInfo
						lgWidth="25%"
						props={{
							instance: info?.kramerius.name,
							url: info?.kramerius.url,
							version: info?.krameriusPlus.version,
						}}
						xsWidth="60%"
					/>
					<ToolbarMenu lgWidth="50%" pages={pages} xsWidth="20%" />
					<NavbarUser width="25%" />
				</Toolbar>
			</Container>
		</AppBar>
	)
}
