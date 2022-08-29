import Keycloak from 'keycloak-js'

// Setup Keycloak instance as needed
// Pass initialization options as required or leave blank to load from 'keycloak.json'
const keycloak = new Keycloak({
	url: process.env.REACT_APP_KEYCLOAK_URL || 'https://keycloak.sekan.eu',
	realm: process.env.REACT_APP_KEYCLOAK_REALM || 'DL4DHFeeder',
	clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || 'feeder',
})

export default keycloak
