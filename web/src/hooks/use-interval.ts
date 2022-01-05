import { useEffect, useRef } from 'react'

type Callback = () => Promise<void>

export const useInterval = (
	callback: Callback,
	delay: number,
	executeCallbackOnMount?: boolean,
) => {
	const savedCallback = useRef<Callback | null>(null)

	useEffect(() => {
		savedCallback.current = callback

		return () => {
			savedCallback.current = null
		}
	}, [callback])

	useEffect(() => {
		if (savedCallback.current != null && executeCallbackOnMount) {
			savedCallback.current()
		}
	}, [executeCallbackOnMount])

	useEffect(() => {
		function tick() {
			if (savedCallback.current != null) {
				savedCallback.current()
			}
		}

		if (delay !== null) {
			const id = setInterval(tick, delay)
			return () => clearInterval(id)
		}
	}, [delay])
}
