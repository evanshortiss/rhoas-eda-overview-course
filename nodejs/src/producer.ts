import { KafkaConfig, Kafka, Producer } from 'kafkajs'
import { get } from 'env-var'
import { songs } from './songs'

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
  
  console.log('Connecting to Kafka cluster...')
  await producer.connect()

  // Send the predefined list of songs
  console.log('Sending messages to Kafka cluster...')
  await producer.send({
    topic: 'songs',
    messages: songs
  })

  console.log('Done!')

  producer.disconnect()
})()
