import { EventProps } from "../../components/event/event-detail"
import { ApiError } from "../../models"

export async function enrich(publications: string[], override?: boolean){

  const requestUrl = override ? '/api/scheduler/schedule?override=true' : '/api/scheduler/schedule'

  try {
    await fetch(requestUrl, {
      method: 'POST',
      headers: new Headers({ 'Content-Type': 'application/json' }),
      body: JSON.stringify({publications})
    })

    return {
      ok: true,
      data: {},
    }
  } catch(e) {
    console.error(e)

    return {
      ok: false,
      data: e as ApiError
    }
  }
}

export async function getRunningTasks():Promise<EventProps[]> {
  try {
    const response = await fetch('/api/scheduler/tasks/queue', {
      method: 'GET'
    })

    const json: EventProps[] = await response.json()

    return json;
  } catch (e) {
    return [];
  }
}

export async function getFinishedTasks():Promise<EventProps[]> {
  try {
    const response = await fetch('/api/scheduler/tasks/finished', {
      method: 'GET'
    })

    const json: EventProps[] = await response.json()

    return json;
  } catch (e) {

    return [];
  }
}

export async function cancelTask(id : string) {
  try {
    const response = await fetch(`/api/scheduler/tasks/cancel/${id}`, {
      method: 'POST'
    })
    return {
      ok: response.ok
    }
  } catch(e) {
    console.error(e)

    return {
      ok: false
    }
  }
}
