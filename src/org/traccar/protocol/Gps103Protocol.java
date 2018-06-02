/*
 * Copyright 2015 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import io.netty.channel.ChannelPipeline;
import org.traccar.AdvancedStringDecoder;
import org.traccar.AdvancedStringEncoder;
import org.traccar.BaseProtocol;
import org.traccar.CharacterDelimiterFrameDecoder;
import org.traccar.TrackerServer;
import org.traccar.model.Command;

import java.util.List;

public class Gps103Protocol extends BaseProtocol {

    public Gps103Protocol() {
        super("gps103");
        setSupportedDataCommands(
                Command.TYPE_CUSTOM,
                Command.TYPE_POSITION_SINGLE,
                Command.TYPE_POSITION_PERIODIC,
                Command.TYPE_POSITION_STOP,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME,
                Command.TYPE_ALARM_ARM,
                Command.TYPE_ALARM_DISARM,
                Command.TYPE_REQUEST_PHOTO);
    }

    @Override
    public void initTrackerServers(List<TrackerServer> serverList) {
        serverList.add(new TrackerServer(false, getName()) {
            @Override
            protected void addProtocolHandlers(ChannelPipeline pipeline) {
                pipeline.addLast("frameDecoder", new CharacterDelimiterFrameDecoder(2048, "\r\n", "\n", ";"));
                pipeline.addLast("stringEncoder", new AdvancedStringEncoder());
                pipeline.addLast("stringDecoder", new AdvancedStringDecoder());
                pipeline.addLast("objectEncoder", new Gps103ProtocolEncoder());
                pipeline.addLast("objectDecoder", new Gps103ProtocolDecoder(Gps103Protocol.this));
            }
        });
        serverList.add(new TrackerServer(true, getName()) {
            @Override
            protected void addProtocolHandlers(ChannelPipeline pipeline) {
                pipeline.addLast("stringEncoder", new AdvancedStringEncoder());
                pipeline.addLast("stringDecoder", new AdvancedStringDecoder());
                pipeline.addLast("objectEncoder", new Gps103ProtocolEncoder());
                pipeline.addLast("objectDecoder", new Gps103ProtocolDecoder(Gps103Protocol.this));
            }
        });
    }

}
