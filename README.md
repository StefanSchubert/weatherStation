# Welcome to this virtual Weather-Station

This small APP collects weather data from open API endpoints and provide them in prometheus-style format.
Thus you can build your own personal weather dashboard using e.g. prometheus, grafana.

To use this app you might require to register to the open API service provider to obtain an API-Key to query the data,
as the api-keys are not provided within this project.

List of currently supported weather data providers are:

* https://www.tomorrow.io
* ...

New suppliers can be integrated, by providing an adapter to this project.

**Notice:** Values will be cached for 30 minutes before a requery hits the
origin data supplier.  

## Usage

### Docker Deployment

    pull accsonaut/weather-station:latest
    docker run -p 8080:8080 -e TomorrowIOapiKey=PLACE_YOU_API_KEY_HERE --name WaetherStation accsonaut/weather-station:latest

point your browser to 

    http://localhost:8080/metrics

(response may need 5 secs)

### Ansible Deployment

I use this to deploy the WeatherStation on a raspberry pi. You may adopt the config to suit your environment.

#### Preconditions to use these scripts

* Ansible and ssh are available
* The ssh private key of the executing user has been published onto the 'pi' account.
* The pi has a java 21 JRE, notice the Environment at your [Service]section in the 
  weatherStation.service file as it should point to the java 21 JRE.

#### Deployment of a new Weather Station release

1) Build the new Weather-Station Release
2) ansible-playbook -i hosts deployWeatherStation.yml

#### Configure API-Key and locations

Copy the application.yml into /var/weatherstation/conf 
Add you API-Key(s), favorite locations and restart the service.

    sudo systemctl restart weatherStation

