/*
 * Copyright 2002-2022 CODESKY.COM Team Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Github:
 *	https://github.com/codesky-com/reb.git
 */

package com.codesky.reb.message;

import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codesky.reb.message.struct.DataPacket;
import com.google.protobuf.Message;

public class MessageDecoder {

	private final static Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);
	
	private final MessageFactory messageFactory;
	
	public MessageDecoder(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}
	
	private boolean checkSign(DataPacket packet) {
		CRC32 crc32 = new CRC32();
		crc32.update(packet.getData());
		return (crc32.getValue() == packet.getSign());
	}
	
	public Message decode(DataPacket packet) {
		if (!checkSign(packet)) {
			LOGGER.error("Invalid signature! cmd={}", Long.toHexString(packet.getCmd()));
			return null;
		}
		
		if (packet.getLength() != (packet.getData().length + DataPacket.HEADER_SIZE)) {
			LOGGER.error("Invalid data length! cmd={}", Long.toHexString(packet.getCmd()));
			return null;
		}
		
		return messageFactory.newMessage(packet.getCmd(), packet.getData());
	}
}
