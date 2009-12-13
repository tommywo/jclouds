/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.aws.s3.config;

import javax.inject.Inject;
import javax.inject.Provider;

import org.jclouds.aws.s3.domain.MutableObjectMetadata;
import org.jclouds.aws.s3.domain.S3Object;
import org.jclouds.aws.s3.domain.internal.S3ObjectImpl;
import org.jclouds.encryption.EncryptionService;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;

/**
 * Configures the domain object mappings needed for all S3 implementations
 * 
 * @author Adrian Cole
 */
public class S3ObjectModule extends AbstractModule {

   /**
    * explicit factories are created here as it has been shown that Assisted Inject is extremely
    * inefficient. http://code.google.com/p/google-guice/issues/detail?id=435
    */
   @Override
   protected void configure() {
      bind(S3Object.Factory.class).to(S3ObjectFactory.class).in(Scopes.SINGLETON);
   }

   private static class S3ObjectFactory implements S3Object.Factory {
      @Inject
      EncryptionService encryptionService;
      @Inject
      Provider<MutableObjectMetadata> metadataProvider;

      public S3Object create(MutableObjectMetadata metadata) {
         return new S3ObjectImpl(encryptionService, metadata != null ? metadata : metadataProvider
                  .get());
      }
   }

   @Provides
   S3Object provideS3Object(S3Object.Factory factory) {
      return factory.create(null);
   }

}