/*
 * Copyright (c) 2020 - Manifold Systems LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package manifold.api.json;

import manifold.json.rt.api.Requester;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import abc.Dummy;
import spark.Spark;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class RequesterTest
{
  @BeforeClass
  public static void init()
  {
    TestServer.main(new String[0]);
    Spark.awaitInitialization();
  }

  @AfterClass
  public static void destroy() {
    TestServer.stop();
  }

  private void assertEqualParameterStrings(String expected, String actual) {
    // Split and sort parameters for both expected and actual strings
    List<String> expectedParams = Arrays.stream(expected.split("&")).sorted().collect(Collectors.toList());
    List<String> actualParams = Arrays.stream(actual.split("&")).sorted().collect(Collectors.toList());

    // Compare sorted parameter lists
    assertEquals(expectedParams, actualParams);
  }

  @Test
  public void httpPostRequestWithParams()
  {
    Requester<Dummy> req = Dummy.request( "http://localhost:4567/" )
            .withParam( "foo", "bar" )
            .withParam( "abc", "8" );
    Object queryString = req.postOne( "testPost_QueryString", Dummy.create(), Requester.Format.Text );
    assertEqualParameterStrings("foo=bar&abc=8", queryString.toString());
  }

  @Test
  public void httpGetRequestWithParams() throws InterruptedException {
    Requester<Dummy> req = Dummy.request( "http://localhost:4567/" )
            .withParam( "foo", "bar" )
            .withParam( "abc", "8" );
    Object queryString = req.getOne( "testGet_QueryString", Dummy.create(), Requester.Format.Text );
    Thread.sleep(1000);
    // assertEquals( "foo=bar&abc=8", queryString );
    assertEqualParameterStrings("foo=bar&abc=8", queryString.toString());

  }

  @Test
  public void httpPostRequestWithParameterizedUrlSuffixWithParams()
  {
    Requester<Dummy> req = Dummy.request( "http://localhost:4567/" )
            .withParam( "foo", "bar" )
            .withParam( "abc", "8" );
    Object queryString = req.postOne( "testPost_QueryString?firstParam=firstValue", Dummy.create(), Requester.Format.Text );
    assertEqualParameterStrings("firstParam=firstValue&foo=bar&abc=8", queryString.toString());
  }

  @Test
  public void httpGetRequestWithParameterizedUrlSuffixWithParams()
  {
    Requester<Dummy> req = Dummy.request( "http://localhost:4567/" )
            .withParam( "foo", "bar" )
            .withParam( "abc", "8" );
    Object queryString = req.getOne( "testGet_QueryString?firstParam=firstValue", Dummy.create(), Requester.Format.Text );
    assertEqualParameterStrings("firstParam=firstValue&foo=bar&abc=8", queryString.toString());
  }
}
