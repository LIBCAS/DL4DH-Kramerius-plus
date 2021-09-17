import { EventProps } from "../../components/event/event-detail"

export async function enrich(publications: string[]){
  try {
    const response = await fetch('/api/scheduler/schedule', {
      method: 'POST',
      headers: new Headers({ 'Content-Type': 'application/json' }),
      body: JSON.stringify({publications})
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

export async function getRunningTasks():Promise<EventProps[]> {
  try {
    const response = await fetch('/api/scheduler/tasks/queue', {
      method: 'GET'
    })

    const json: EventProps[] = await response.json()

    return json;
  } catch (e) {
    return []
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

    return []
  }
}
