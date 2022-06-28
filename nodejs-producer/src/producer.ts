import { KafkaConfig, Kafka } from 'kafkajs'
import { get } from 'env-var'
import { songs } from './songs'
import log from 'barelog'

(async function main () {
  // Read in environment variables and create a KafkaConfig
  const brokers = get('BOOTSTRAP_SERVER').required().asArray()
  const username = get('CLIENT_ID').required().asString()
  const password = get('CLIENT_SECRET').required().asString()
  const cfg: KafkaConfig = {
    ssl: true,
    brokers,
    sasl: {
      mechanism: 'plain',
      username,
      password,
    }
  }
  
  // Create a producer and connect the producer to the cluster
  const kafka = new Kafka(cfg)
  const producer = kafka.producer()
  
  log('Connecting to Kafka cluster')
  await producer.connect()

  const getRandomSong = () => {
    const max = songs.length - 1;
    const idx = Math.floor(Math.random() * max);

    return songs[idx]
  }

  const sendSong = async () => {
    const song = getRandomSong()
    
    log(`Sending song "${song.value}" by "${song.key}" to the songs topic in Kafka`)
    
    try {
      await producer.send({
        topic: 'songs',
        messages: [song]
      })

      // Send another song in 2 seconds
      setTimeout(sendSong, 2000)
    } catch (e) {
      log('Error sending song to Kafka:', e)
    }
  }

  const exit = () => {
    log('Disconnecting from Kafka')
    producer.disconnect()
      .then(() => process.exit(0))
      .catch((e) => {
        log('Failed to gracefully disconnect from Kafka exiting with code 1', e)
        process.exit(1)
      })
  }

  process.on('SIGTERM', exit)
  process.on('SIGINT', exit)

  log('Start sending messages to Kafka cluster')

  // Sends an initial song, then continues to send one approx every 2 seconds
  sendSong()
})()
