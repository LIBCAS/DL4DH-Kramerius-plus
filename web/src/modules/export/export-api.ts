import { FileRef, Publication } from 'models';
import fetch from 'utils/fetch'

export const getPublications = async () => {
  try {
    const response = await fetch('/api/publication/list', {
      method: 'GET',
      headers: new Headers({ 'Content-Type': 'application/json' }),
    });

    const json: Publication[] = await response.json();

    return json;
  } catch (e) {
    return [];
  }
};

export const getExportedPublications = async () => {
  try {
    const response = await fetch('/api/export/list', {
      method: 'GET',
      headers: new Headers({ 'Content-Type': 'application/json' }),
    });

    const json: FileRef[] = await response.json();

    return json;
  } catch (e) {
    return [];
  }
};
