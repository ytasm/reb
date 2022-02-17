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

import com.codesky.reb.message.struct.DataPacket;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Message;

public class MessageEncoder {

	private final MessageFactory messageFactory;

	public MessageEncoder(MessageFactory messageFactory) {
		this.messageFactory = messageFactory;
	}

	private long sign(byte[] data) {
		CRC32 crc32 = new CRC32();
		crc32.update(data);
		return crc32.getValue();
	}

	public DataPacket encode(Message protoMsg) {
		Descriptor descriptor = protoMsg.getDescriptorForType();
		byte[] data = protoMsg.toByteArray();
		long cmd = messageFactory.findMessageCmdByPackageName(descriptor.getFullName());
		long sign = sign(data);
		return new DataPacket(cmd, 0, sign, data);
	}
}
