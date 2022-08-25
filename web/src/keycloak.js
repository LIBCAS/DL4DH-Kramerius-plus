import Keycloak from 'keycloak-js'

// Setup Keycloak instance as needed
// Pass initialization options as required or leave blank to load from 'keycloak.json'
const keycloak = new Keycloak({
	url: 'https://keycloak.sekan.eu',
	realm: 'DL4DHFeeder',
	clientId: 'feeder',
})

export default keycloak
