import { ReactNode } from 'react'

import { Dialog } from './dialog'
import { DialogContext } from './dialog-context'
import { useDialog } from './dialog-context-hook'

export function DialogProvider({ children }: { children: ReactNode }) {
	const { dialogCtx, dialog } = useDialog()

	return (
		<DialogContext.Provider value={dialogCtx}>
			{children}

			<Dialog
				Content={dialog.Content}
				initialValues={dialog.initialValues}
				opened={dialog.opened}
				size={dialog.size}
				onClose={dialogCtx.close}
			/>
		</DialogContext.Provider>
	)
}
