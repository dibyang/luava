/**
 * Copyright (c) 2008, http://www.snakeyaml.org
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
package org.yaml.snakeyaml.representer;


import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represent JavaBeans
 * @author admin
 */
public class Representer2 extends Representer {
  public Representer2() {
    super();
  }

  public Representer2(DumperOptions options) {
    super(options);
  }

  @Override
  protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
    List<NodeTuple> value = new ArrayList<NodeTuple>(properties.size());
    Tag tag;
    Tag customTag = classTags.get(javaBean.getClass());
    tag = customTag != null ? customTag : Tag.MAP;//new Tag(javaBean.getClass());
    // flow style will be chosen by BaseRepresenter
    MappingNode node = new MappingNode(tag, value, DumperOptions.FlowStyle.AUTO);
    representedObjects.put(javaBean, node);
    DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
    for (Property property : properties) {
      Object memberValue = property.get(javaBean);
      Tag customPropertyTag = memberValue == null ? null
        : classTags.get(memberValue.getClass());
      NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue,
        customPropertyTag);
      if (tuple == null) {
        continue;
      }
      if (!((ScalarNode) tuple.getKeyNode()).isPlain()) {
        bestStyle = DumperOptions.FlowStyle.BLOCK;
      }
      Node nodeValue = tuple.getValueNode();
      if (!(nodeValue instanceof ScalarNode && ((ScalarNode) nodeValue).isPlain())) {
        bestStyle = DumperOptions.FlowStyle.BLOCK;
      }
      value.add(tuple);
    }
    if (defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
      node.setFlowStyle(defaultFlowStyle);
    } else {
      node.setFlowStyle(bestStyle);
    }
    return node;
  }
}
